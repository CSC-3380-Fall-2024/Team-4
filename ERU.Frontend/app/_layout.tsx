import React from 'react';
import { Tabs } from 'expo-router';

const Layout = () => {
  return (
    <Tabs>
      <Tabs.Screen name="index" options={{ title: 'Home' }} />
      <Tabs.Screen name="explore" options={{ title: 'Explore' }} />
      <Tabs.Screen name="homefeed" options={{ title: 'Homefeed' }} />
      <Tabs.Screen name="upload" options={{ title: 'Upload' }} />
    </Tabs>
  );
};

export default Layout;
