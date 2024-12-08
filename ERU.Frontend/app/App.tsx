import React from 'react';
import { Stack } from 'expo-router';

const App: React.FC = () => {
  return (
    <Stack
      screenOptions={{
        headerShown: false, // Hide headers if not needed
      }}
    />
  );
};

export default App;
