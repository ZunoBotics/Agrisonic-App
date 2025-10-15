# Laravel Backend - Quick Start Checklist

## ✅ Your Action Items

Follow this checklist to set up the Laravel backend:

### Phase 1: Local Setup (30 minutes)

- [ ] **Install PHP 8.2+**
  ```bash
  php -v  # Check version
  ```
  If not installed: Download from [php.net](https://www.php.net/downloads)

- [ ] **Install Composer**
  ```bash
  composer --version  # Check if installed
  ```
  If not installed: [getcomposer.org/download](https://getcomposer.org/download/)

- [ ] **Create Laravel Project**
  ```bash
  cd /Users/jonathan/Agrisonic
  composer create-project laravel/laravel agrisonic-backend
  cd agrisonic-backend
  ```

- [ ] **Install Dependencies**
  ```bash
  composer require laravel/sanctum
  php artisan vendor:publish --provider="Laravel\Sanctum\SanctumServiceProvider"
  ```

- [ ] **Get Neon Database Credentials**
  1. Go to [console.neon.tech](https://console.neon.tech)
  2. Select your Agrisonic project
  3. Click "Connection Details"
  4. Copy these values:
     - Host (e.g., `ep-xxx.neon.tech`)
     - Database name
     - Username
     - Password

- [ ] **Configure `.env` File**
  ```env
  DB_CONNECTION=pgsql
  DB_HOST=your-neon-host.neon.tech
  DB_PORT=5432
  DB_DATABASE=neondb
  DB_USERNAME=your-username
  DB_PASSWORD=your-password
  DB_SSLMODE=require
  ```

- [ ] **Generate Application Key**
  ```bash
  php artisan key:generate
  ```

- [ ] **Copy Migration & Controller Files**
  - Copy User migration from the guide
  - Copy AuthController from the guide
  - Update `routes/api.php` from the guide

- [ ] **Run Migrations**
  ```bash
  php artisan migrate
  ```

- [ ] **Test Locally**
  ```bash
  php artisan serve
  # Test: http://localhost:8000/api/auth/signup
  ```

---

### Phase 2: Railway Deployment (15 minutes)

- [ ] **Create Railway Account**
  1. Go to [railway.app](https://railway.app)
  2. Click "Start a New Project"
  3. Login with GitHub (no credit card needed)

- [ ] **Create `Procfile`** in project root:
  ```
  web: php artisan serve --host=0.0.0.0 --port=$PORT
  ```

- [ ] **Create `nixpacks.toml`** in project root:
  ```toml
  [phases.setup]
  nixPkgs = ["php82", "php82Extensions.pdo_pgsql", "php82Extensions.pgsql"]

  [phases.install]
  cmds = ["composer install --no-dev --optimize-autoloader"]

  [start]
  cmd = "php artisan serve --host=0.0.0.0 --port=$PORT"
  ```

- [ ] **Push to GitHub**
  ```bash
  git init
  git add .
  git commit -m "Initial Laravel backend"
  git branch -M main
  git remote add origin https://github.com/ZunoBotics/agrisonic-backend.git
  git push -u origin main
  ```

- [ ] **Deploy on Railway**
  1. Click "New Project" on Railway dashboard
  2. Choose "Deploy from GitHub repo"
  3. Select `agrisonic-backend` repo
  4. Railway auto-detects Laravel

- [ ] **Add Environment Variables on Railway**
  Click "Variables" tab and add:
  ```
  APP_KEY=<generate with: php artisan key:generate --show>
  DB_CONNECTION=pgsql
  DB_HOST=your-neon-host.neon.tech
  DB_PORT=5432
  DB_DATABASE=neondb
  DB_USERNAME=your-username
  DB_PASSWORD=your-password
  DB_SSLMODE=require
  APP_ENV=production
  APP_DEBUG=false
  APP_URL=https://your-app.up.railway.app
  ```

- [ ] **Run Migrations on Railway**
  ```bash
  # Install Railway CLI
  npm install -g @railway/cli

  # Login and link
  railway login
  railway link

  # Run migration
  railway run php artisan migrate --force
  ```

- [ ] **Get Your Railway URL**
  - Go to Settings → Domains
  - Copy the URL (e.g., `https://agrisonic-backend.up.railway.app`)

---

### Phase 3: Update Android App (5 minutes)

- [ ] **Update API Base URL**

  Edit `agrisonic-app/app/build.gradle`:
  ```gradle
  buildConfigField "String", "API_BASE_URL", "\"https://your-app.up.railway.app\""
  ```
  Replace with your actual Railway URL (without trailing slash)

- [ ] **Update Token Handling (if needed)**

  The current Android app uses cookies. For Laravel Sanctum tokens, update `RetrofitInstance.kt`:
  ```kotlin
  private val client = OkHttpClient.Builder()
      .addInterceptor { chain ->
          val request = chain.request().newBuilder()
          val token = preferencesManager.getSessionToken()
          if (!token.isNullOrEmpty()) {
              request.addHeader("Authorization", "Bearer $token")
          }
          chain.proceed(request.build())
      }
      .build()
  ```

- [ ] **Update Login Response Handling**

  Edit `LoginActivity.kt` to save the token:
  ```kotlin
  val token = response.body()?.data?.token
  if (token != null) {
      preferencesManager.saveSessionToken(token)
  }
  ```

- [ ] **Rebuild Android App**
  ```bash
  cd agrisonic-app
  ./gradlew assembleDebug
  ```

- [ ] **Install & Test**
  ```bash
  ~/Library/Android/sdk/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk
  ```

---

### Phase 4: Testing (10 minutes)

- [ ] **Test Signup**
  - Open Android app
  - Click "Sign Up"
  - Enter name, email, password
  - Should succeed and show verification screen

- [ ] **Test Login**
  - Go back to login
  - Enter email and password
  - Should login successfully

- [ ] **Check Railway Logs**
  ```bash
  railway logs
  ```
  Should see API requests coming in

- [ ] **Test Logout**
  - Click logout in app
  - Should clear session and return to login

---

## 🎯 Quick Test Commands

### Test API Endpoints Locally:

```bash
# Signup
curl -X POST http://localhost:8000/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123","confirmPassword":"password123"}'

# Login
curl -X POST http://localhost:8000/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Verify Signup
curl -X POST http://localhost:8000/api/auth/verify-signup \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","code":"123456"}'
```

### Test API on Railway:

Replace `http://localhost:8000` with your Railway URL:
```bash
curl -X POST https://your-app.up.railway.app/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123","confirmPassword":"password123"}'
```

---

## ⏱️ Total Time Estimate

- Phase 1 (Local Setup): ~30 minutes
- Phase 2 (Railway Deploy): ~15 minutes
- Phase 3 (Android Update): ~5 minutes
- Phase 4 (Testing): ~10 minutes

**Total: ~60 minutes (1 hour)**

---

## 🆘 Common Issues & Solutions

### "Class 'PDO' not found"
```bash
# Install PHP PostgreSQL extension
# Mac: brew install php@8.2-pgsql
# Ubuntu: sudo apt-get install php-pgsql
```

### "SQLSTATE[08006] Connection refused"
- Check Neon credentials are correct
- Ensure `DB_SSLMODE=require` is set
- Verify database exists in Neon console

### Railway build fails
- Check `nixpacks.toml` is in project root
- Verify all environment variables are set
- Check logs: `railway logs`

### Android app can't connect
- Ensure Railway URL is correct (include `https://`)
- Check CORS is configured in Laravel
- Verify API endpoints match (e.g., `/api/auth/signin`)

---

## 📝 What You'll Have After This

✅ Laravel backend running on Railway (free tier)
✅ Connected to your existing Neon PostgreSQL database
✅ Full authentication API (signup, login, logout, password reset)
✅ Android app connecting to Laravel instead of Next.js
✅ Scalable backend ready for more features

---

## 🚀 Next Steps After Setup

Once everything is working:

1. Add email verification (use Mailtrap for testing)
2. Add more API endpoints (weather, crops, market prices)
3. Add rate limiting (Laravel has this built-in)
4. Add API documentation (use Scribe or L5-Swagger)
5. Set up monitoring (Railway has built-in metrics)

---

## 💡 Pro Tips

- **Keep `.env` secure**: Never commit it to Git
- **Use Railway CLI**: Faster than web dashboard for logs/migrations
- **Monitor usage**: Check Railway dashboard for credit usage
- **Database backups**: Neon has automatic backups
- **SSL is automatic**: Railway provides HTTPS for free

---

Ready to start? Begin with **Phase 1: Local Setup** and check off each item as you go! 🚀
