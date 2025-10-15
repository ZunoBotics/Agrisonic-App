# Laravel Backend Setup for Agrisonic

Complete guide to set up Laravel backend with Neon PostgreSQL and deploy to Railway.

## 🎯 Overview

We're replacing the Next.js API with Laravel to communicate with the Neon PostgreSQL database. Laravel provides:
- Better PHP ecosystem for backend APIs
- Built-in authentication with Sanctum
- Robust database migrations and ORM (Eloquent)
- Easy deployment to Railway (free tier available)

## 📋 Prerequisites

Before starting, ensure you have:
- PHP 8.2 or higher installed
- Composer installed ([getcomposer.org](https://getcomposer.org))
- PostgreSQL client (optional, for local testing)
- Railway account ([railway.app](https://railway.app)) - **FREE tier available**
- Your Neon database connection string

---

## Part 1: Local Laravel Setup

### Step 1: Create Laravel Project

```bash
# Navigate to your Agrisonic directory
cd /Users/jonathan/Agrisonic

# Create new Laravel project
composer create-project laravel/laravel agrisonic-backend

# Navigate into the project
cd agrisonic-backend
```

### Step 2: Install Required Packages

```bash
# Install Laravel Sanctum for API authentication
composer require laravel/sanctum

# Install CORS package (already included in Laravel 11+)
# No need to install separately

# Publish Sanctum configuration
php artisan vendor:publish --provider="Laravel\Sanctum\SanctumServiceProvider"
```

### Step 3: Configure Environment Variables

Edit `.env` file:

```env
APP_NAME=Agrisonic
APP_ENV=production
APP_KEY=base64:GENERATE_THIS_WITH_php_artisan_key:generate
APP_DEBUG=false
APP_URL=http://localhost

# Database - Replace with your Neon PostgreSQL credentials
DB_CONNECTION=pgsql
DB_HOST=your-neon-hostname.neon.tech
DB_PORT=5432
DB_DATABASE=your_database_name
DB_USERNAME=your_username
DB_PASSWORD=your_password
DB_SSLMODE=require

# Session and Cache
SESSION_DRIVER=database
CACHE_DRIVER=database
QUEUE_CONNECTION=database

# Sanctum
SANCTUM_STATEFUL_DOMAINS=localhost:3000,agrisonic-app
SESSION_DOMAIN=.railway.app
```

**To get your Neon credentials:**
1. Go to [Neon Console](https://console.neon.tech)
2. Select your project
3. Go to "Connection Details"
4. Copy the PostgreSQL connection string
5. Extract: hostname, database, username, password

### Step 4: Generate Application Key

```bash
php artisan key:generate
```

### Step 5: Configure Database for PostgreSQL

Edit `config/database.php` - ensure PostgreSQL config looks like this:

```php
'pgsql' => [
    'driver' => 'pgsql',
    'url' => env('DATABASE_URL'),
    'host' => env('DB_HOST', '127.0.0.1'),
    'port' => env('DB_PORT', '5432'),
    'database' => env('DB_DATABASE', 'forge'),
    'username' => env('DB_USERNAME', 'forge'),
    'password' => env('DB_PASSWORD', ''),
    'charset' => 'utf8',
    'prefix' => '',
    'prefix_indexes' => true,
    'search_path' => 'public',
    'sslmode' => env('DB_SSLMODE', 'prefer'),
],
```

---

## Part 2: Create Database Migrations & Models

### Step 1: Create Users Migration

Laravel already has a users migration. Update it:

```bash
# Edit database/migrations/xxxx_create_users_table.php
```

```php
<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('users', function (Blueprint $table) {
            $table->id();
            $table->string('name');
            $table->string('email')->unique();
            $table->timestamp('email_verified_at')->nullable();
            $table->string('password');
            $table->boolean('is_verified')->default(false);
            $table->boolean('is_admin')->default(false);
            $table->string('verification_code')->nullable();
            $table->timestamp('verification_code_expires_at')->nullable();
            $table->rememberToken();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('users');
    }
};
```

### Step 2: Create Additional Migrations

```bash
# Create sessions table
php artisan session:table

# Create personal access tokens table (for Sanctum)
php artisan migrate
```

### Step 3: Update User Model

Edit `app/Models/User.php`:

```php
<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class User extends Authenticatable
{
    use HasApiTokens, HasFactory, Notifiable;

    protected $fillable = [
        'name',
        'email',
        'password',
        'is_verified',
        'is_admin',
        'verification_code',
        'verification_code_expires_at',
    ];

    protected $hidden = [
        'password',
        'remember_token',
        'verification_code',
    ];

    protected $casts = [
        'email_verified_at' => 'datetime',
        'verification_code_expires_at' => 'datetime',
        'is_verified' => 'boolean',
        'is_admin' => 'boolean',
        'password' => 'hashed',
    ];
}
```

---

## Part 3: Create API Routes & Controllers

### Step 1: Create Auth Controller

```bash
php artisan make:controller Api/AuthController
```

Edit `app/Http/Controllers/Api/AuthController.php`:

```php
<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;
use Illuminate\Support\Str;
use Carbon\Carbon;

class AuthController extends Controller
{
    public function signup(Request $request)
    {
        $request->validate([
            'name' => 'required|string|max:255',
            'email' => 'required|string|email|max:255|unique:users',
            'password' => 'required|string|min:8',
            'confirmPassword' => 'required|same:password',
        ]);

        $verificationCode = rand(100000, 999999);

        $user = User::create([
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password),
            'verification_code' => $verificationCode,
            'verification_code_expires_at' => Carbon::now()->addMinutes(30),
            'is_verified' => false,
        ]);

        // TODO: Send verification email
        // Mail::to($user->email)->send(new VerificationCode($verificationCode));

        return response()->json([
            'success' => true,
            'message' => 'User registered successfully. Please verify your email.',
        ], 201);
    }

    public function signin(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
            'password' => 'required',
        ]);

        $user = User::where('email', $request->email)->first();

        if (!$user || !Hash::check($request->password, $user->password)) {
            return response()->json([
                'success' => false,
                'message' => 'Invalid credentials',
            ], 401);
        }

        $token = $user->createToken('auth-token')->plainTextToken;

        return response()->json([
            'success' => true,
            'data' => [
                'user' => [
                    'id' => $user->id,
                    'name' => $user->name,
                    'email' => $user->email,
                    'isVerified' => $user->is_verified,
                    'isAdmin' => $user->is_admin,
                ],
                'token' => $token,
            ],
        ]);
    }

    public function verifySignup(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
            'code' => 'required|string',
        ]);

        $user = User::where('email', $request->email)->first();

        if (!$user) {
            return response()->json([
                'success' => false,
                'message' => 'User not found',
            ], 404);
        }

        if ($user->verification_code !== $request->code) {
            return response()->json([
                'success' => false,
                'message' => 'Invalid verification code',
            ], 400);
        }

        if (Carbon::now()->greaterThan($user->verification_code_expires_at)) {
            return response()->json([
                'success' => false,
                'message' => 'Verification code expired',
            ], 400);
        }

        $user->update([
            'is_verified' => true,
            'email_verified_at' => Carbon::now(),
            'verification_code' => null,
            'verification_code_expires_at' => null,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Email verified successfully',
        ]);
    }

    public function sendVerificationCode(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
            'type' => 'required|in:signup,password_change',
        ]);

        $user = User::where('email', $request->email)->first();

        if (!$user) {
            return response()->json([
                'success' => false,
                'message' => 'User not found',
            ], 404);
        }

        $verificationCode = rand(100000, 999999);

        $user->update([
            'verification_code' => $verificationCode,
            'verification_code_expires_at' => Carbon::now()->addMinutes(30),
        ]);

        // TODO: Send verification email
        // Mail::to($user->email)->send(new VerificationCode($verificationCode));

        return response()->json([
            'success' => true,
            'message' => 'Verification code sent successfully',
        ]);
    }

    public function changePassword(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
            'newPassword' => 'required|string|min:8',
            'confirmPassword' => 'required|same:newPassword',
        ]);

        $user = User::where('email', $request->email)->first();

        if (!$user) {
            return response()->json([
                'success' => false,
                'message' => 'User not found',
            ], 404);
        }

        $user->update([
            'password' => Hash::make($request->newPassword),
            'verification_code' => null,
            'verification_code_expires_at' => null,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Password changed successfully',
        ]);
    }

    public function signout(Request $request)
    {
        $request->user()->currentAccessToken()->delete();

        return response()->json([
            'success' => true,
            'message' => 'Logged out successfully',
        ]);
    }
}
```

### Step 2: Define API Routes

Edit `routes/api.php`:

```php
<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController;

// Public routes
Route::post('/auth/signup', [AuthController::class, 'signup']);
Route::post('/auth/signin', [AuthController::class, 'signin']);
Route::post('/auth/verify-signup', [AuthController::class, 'verifySignup']);
Route::post('/auth/send-verification-code', [AuthController::class, 'sendVerificationCode']);
Route::post('/auth/change-password', [AuthController::class, 'changePassword']);

// Protected routes
Route::middleware('auth:sanctum')->group(function () {
    Route::post('/auth/signout', [AuthController::class, 'signout']);
    Route::get('/user', function (Request $request) {
        return $request->user();
    });
});
```

### Step 3: Configure CORS

Edit `config/cors.php`:

```php
<?php

return [
    'paths' => ['api/*', 'sanctum/csrf-cookie'],
    'allowed_methods' => ['*'],
    'allowed_origins' => ['*'], // In production, specify your Android app
    'allowed_origins_patterns' => [],
    'allowed_headers' => ['*'],
    'exposed_headers' => [],
    'max_age' => 0,
    'supports_credentials' => true,
];
```

---

## Part 4: Deploy to Railway (FREE)

### Step 1: Create Railway Account

1. Go to [railway.app](https://railway.app)
2. Sign up with GitHub (FREE tier includes $5 monthly credit)
3. No credit card required for free tier!

### Step 2: Prepare for Deployment

Create `Procfile` in project root:

```
web: php artisan serve --host=0.0.0.0 --port=$PORT
```

Create `nixpacks.toml` in project root:

```toml
[phases.setup]
nixPkgs = ["php82", "php82Extensions.pdo_pgsql", "php82Extensions.pgsql"]

[phases.install]
cmds = ["composer install --no-dev --optimize-autoloader"]

[phases.build]
cmds = [
    "php artisan config:cache",
    "php artisan route:cache",
    "php artisan view:cache"
]

[start]
cmd = "php artisan serve --host=0.0.0.0 --port=$PORT"
```

### Step 3: Deploy to Railway

```bash
# Install Railway CLI
npm i -g @railway/cli

# Login to Railway
railway login

# Initialize project
railway init

# Link to your project (or create new)
railway link

# Add PostgreSQL database (optional - you're using Neon)
# railway add

# Set environment variables
railway variables set APP_KEY=$(php artisan key:generate --show)
railway variables set DB_HOST=your-neon-host.neon.tech
railway variables set DB_PORT=5432
railway variables set DB_DATABASE=your_database
railway variables set DB_USERNAME=your_username
railway variables set DB_PASSWORD=your_password
railway variables set DB_SSLMODE=require
railway variables set APP_ENV=production
railway variables set APP_DEBUG=false

# Deploy
railway up

# Run migrations
railway run php artisan migrate --force

# Get your deployment URL
railway domain
```

### Alternative: Deploy via Railway Dashboard

1. Go to [railway.app/new](https://railway.app/new)
2. Click "Deploy from GitHub repo"
3. Select your `agrisonic-backend` repository
4. Railway auto-detects Laravel
5. Add environment variables in the dashboard:
   - `APP_KEY` - Generate with `php artisan key:generate --show`
   - `DB_HOST`, `DB_PORT`, `DB_DATABASE`, `DB_USERNAME`, `DB_PASSWORD`
   - `DB_CONNECTION=pgsql`
   - `DB_SSLMODE=require`
   - `APP_ENV=production`
   - `APP_DEBUG=false`
6. Click "Deploy"
7. Get your public URL from settings (e.g., `https://agrisonic-backend.up.railway.app`)

---

## Part 5: Update Android App

### Step 1: Update API Base URL

Edit `agrisonic-app/app/build.gradle`:

```gradle
buildConfigField "String", "API_BASE_URL", "\"https://your-app.up.railway.app\""
```

Replace `your-app.up.railway.app` with your Railway URL.

### Step 2: Update Retrofit Configuration

The current setup already supports bearer token authentication. Just ensure you're using the token from login response.

Edit `RetrofitInstance.kt` if needed to add bearer token:

```kotlin
private val client = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request = chain.request().newBuilder()

        // Add bearer token if available
        val token = preferencesManager.getSessionToken()
        if (!token.isNullOrEmpty()) {
            request.addHeader("Authorization", "Bearer $token")
        }

        chain.proceed(request.build())
    }
    .build()
```

### Step 3: Test the Connection

1. Build the Android app
2. Try to signup/login
3. Check Railway logs: `railway logs`

---

## 🔍 Testing Locally

Before deploying, test locally:

```bash
# Run migrations
php artisan migrate

# Start server
php artisan serve

# Test endpoint
curl -X POST http://localhost:8000/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

---

## 📊 Railway Free Tier Limits

Railway's free tier includes:
- ✅ $5 monthly credit (enough for small apps)
- ✅ 500MB RAM
- ✅ 1GB disk
- ✅ No credit card required
- ✅ Custom domains
- ✅ Automatic SSL

**Your Laravel app should easily fit within these limits!**

---

## 🚀 Next Steps

1. ✅ Set up Laravel project locally
2. ✅ Configure Neon database connection
3. ✅ Create migrations and models
4. ✅ Build API endpoints
5. ✅ Deploy to Railway
6. ✅ Update Android app configuration
7. 🔄 Test authentication flow
8. 📧 Add email verification (optional - use Mailtrap for testing)

---

## 📝 Quick Command Reference

```bash
# Local development
php artisan serve
php artisan migrate
php artisan make:controller Api/SomeController
php artisan make:model SomeModel -m

# Railway deployment
railway login
railway up
railway logs
railway run php artisan migrate --force
railway variables set KEY=value

# Testing
php artisan test
php artisan tinker
```

---

## 🔒 Security Notes

1. **Never commit `.env` file** - it's in `.gitignore` by default
2. **Use environment variables** for all sensitive data
3. **Enable rate limiting** on auth endpoints (Laravel has this built-in)
4. **Use HTTPS** in production (Railway provides this automatically)
5. **Validate all inputs** (shown in controllers above)

---

## 🆘 Troubleshooting

### Database Connection Failed
- Check Neon console for correct credentials
- Ensure `DB_SSLMODE=require` is set
- Verify firewall allows Railway IP

### Migration Errors
- Run: `railway run php artisan migrate:fresh --force`
- Check database exists in Neon

### 500 Errors
- Check logs: `railway logs`
- Ensure `APP_KEY` is set
- Verify all environment variables

---

## 📚 Resources

- [Laravel Documentation](https://laravel.com/docs)
- [Railway Documentation](https://docs.railway.app)
- [Laravel Sanctum](https://laravel.com/docs/sanctum)
- [Neon PostgreSQL](https://neon.tech/docs)

---

**You're all set!** 🎉

Follow these steps and you'll have a fully functional Laravel backend connected to Neon PostgreSQL, deployed on Railway for free!
