 🍽️ Food Planner App

 📌 Overview
Food Planner is an Android application designed to help users efficiently plan their meals. It provides a calendar-based interface to schedule meals and offers cloud backup functionality using Firebase. 
The app also includes offline support through a local Room database.

 🚀 Features
- 📅 **Weekly Meal Planner**: Schedule meals for each day of the week.
- 🔄 **Sync with Firebase**: Backup and restore planned meals.
- 💾 **Offline Support**: Data is stored locally using Room database.
- 👥 **User Authentication**: Sign up and log in using Firebase Authentication.
- 🔐 **Secure Logout**: Sign out from Firebase anytime.

## 🛠️ Technologies Used
- **Java** for Android development
- **Room Database** for local storage
- **Firebase Firestore** for cloud storage
- **Firebase Authentication** for user management
- **SharedPreferences** for user settings
- **ConstraintLayout** for UI design

 📲 Installation
1. Clone the repository:
    git clone https://github.com/nermeen03/FoodPlanner.git
2. Open the project in **Android Studio**.
3. Sync Gradle and build the project.
4. Run the app on an emulator or a physical device.

 ⚙️ Configuration
To enable Firebase features:
1. **Add Firebase to Your Project**:
   - Download 'google-services.json' from Firebase Console.
   - Place it in the 'app/' directory.
2. **Enable Firebase Authentication**:
   - In Firebase Console, go to Authentication → Sign-in method → Enable Email/Password sign-in and Google.
3. **Setup Firestore Database**:
   - Navigate to Firebase Firestore and create a database in test mode.

