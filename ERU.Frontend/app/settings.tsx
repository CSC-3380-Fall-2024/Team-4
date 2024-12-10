//Still need to finish theme button and faq page
import React, { useState } from 'react';
import { View, Text, StyleSheet, Switch, Pressable } from 'react-native';
import { useRouter } from 'expo-router';

const Settings: React.FC = () => {
  const router = useRouter(); 

    const navigateToFAQ = () => {
    router.push('/faq'); // Redirect to the FAQ page
  };

  const [isDarkMode, setIsDarkMode] = useState(false); // For toggling between dark and light theme
  const toggleSwitch = () => {

    setIsDarkMode((previousState) => !previousState); // Toggle theme
  };

  return (
    <View style={[styles.container, isDarkMode ? styles.darkBackground : styles.lightBackground]}>
      <View style={styles.themeContainer}>
        <Text style={[styles.themeText, isDarkMode ? styles.darkText : styles.lightText]}>Dark Theme</Text>
        <Switch
          value={isDarkMode} 
          onValueChange={toggleSwitch} 
        
        />
       
      </View>
      <Pressable onPress={navigateToFAQ}>
        <Text style={[styles.themeText, isDarkMode ? styles.darkText : styles.lightText]}>FAQ</Text>
      </Pressable>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 50,
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
    top: 100,
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
