import React from 'react';
import { Tabs } from 'expo-router';

const Layout = () => {
  return (
    <Tabs>
      <Tabs.Screen name="index" options={{ title: 'Home', headerShown: false }} />
      <Tabs.Screen name="explore" options={{ title: 'Explore', headerShown: false  }} />
      <Tabs.Screen name="homefeed" options={{ title: 'Homefeed', headerShown: false  }} />
      <Tabs.Screen name="upload" options={{ title: 'Upload', headerShown: false  }} />
      <Tabs.Screen name="settings" options={{ title: 'Settings', headerShown: false  }} />
    </Tabs>
  );
};

export default Layout;
