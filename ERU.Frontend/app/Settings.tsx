import React from 'react';
import { View, Text, StyleSheet, Button, Alert } from 'react-native';

const Settings = () => {
  const handlePress = () => {
    Alert.alert("Settings", "This is a placeholder action for the settings page.");
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Settings Page</Text>
      <Button title="Do Something" onPress={handlePress} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f9f9f9',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
});

export default Settings;
