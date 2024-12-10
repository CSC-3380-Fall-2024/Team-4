import React from 'react';
import { AppRegistry } from 'react-native';
import { View, Text, StyleSheet } from 'react-native';
import { expo as expoRoot } from '../app.json';

import Login from './components/Login';

const appName : string = expoRoot.name;

const Home: React.FC = () => {
  return (
    <Login/>
  );
};

export default Home;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    fontSize: 20,
    fontWeight: 'bold',
  },
});