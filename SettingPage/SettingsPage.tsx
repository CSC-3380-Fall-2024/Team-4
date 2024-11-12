import React, { useState } from 'react';
import './SettingsPage.css';

// Define the type for the form state
interface FormState {
  darkMode: boolean;
  emailNotifications: boolean;
  pushNotifications: boolean;
}

const SettingsPage: React.FC = () => {
  // Specify the type for the form state
  const [form, setForm] = useState<FormState>({
    darkMode: false,
    emailNotifications: true,
    pushNotifications: false,
  });

  return (
    <div className="container">
      <div className="settings-page">
        <div className="profile">
          {/* Updated the image path here */}
          <img src="./profile.jpg" alt="Profile" />
          <h2>John Doe</h2>
          <p>777 Ben Hur Rd, Baton Rouge, LA 17101</p>
        </div>

        <div className="section">
          <div className="section-title">Preferences</div>

          <div className="row">
            <label>Dark Mode</label>
            <input
              type="checkbox"
              checked={form.darkMode}
              onChange={() => setForm({ ...form, darkMode: !form.darkMode })}
            />
          </div>

          <div className="row">
            <label>Email Notifications</label>
            <input
              type="checkbox"
              checked={form.emailNotifications}
              onChange={() =>
                setForm({ ...form, emailNotifications: !form.emailNotifications })
              }
            />
          </div>

          <div className="row">
            <label>Push Notifications</label>
            <input
              type="checkbox"
              checked={form.pushNotifications}
              onChange={() =>
                setForm({ ...form, pushNotifications: !form.pushNotifications })
              }
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default SettingsPage;
