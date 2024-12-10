import React, { useState } from 'react';
import { View, Text, StyleSheet, Switch } from 'react-native';

const Settings: React.FC = () => {
  const [isDarkMode, setIsDarkMode] = useState(false); // For toggling between dark and light theme

  const toggleSwitch = () => {
    setIsDarkMode((previousState) => !previousState); // Toggle theme
  };

  return (
    <View style={[styles.container, isDarkMode ? styles.darkBackground : styles.lightBackground]}>
      <View style={styles.themeContainer}>
        <Text style={[styles.themeText, isDarkMode ? styles.darkText : styles.lightText]}>Dark Theme</Text>
        <Switch
        
        />
       
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    borderRadius: 8,
    justifyContent: 'flex-start', 
  },
  lightBackground: {
    backgroundColor: '#f9f9f9',
  },
  darkBackground: {
    backgroundColor: '#333',
  },
  themeContainer: {
    position: 'absolute', 
    top: 20,
    left: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: 'auto', 
  },
  themeText: {
    fontSize: 18,
    fontWeight: '500',
    marginRight: 10, 
  },
  lightText: {
    color: '#333',
  },
  darkText: {
    color: '#fff',
  },
});

export default Settings;

