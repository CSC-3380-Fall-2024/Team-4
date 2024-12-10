import React, { useState } from 'react';
import { View, Text, Button, StyleSheet, Image } from 'react-native';

const FAQ: React.FC = () => {
  const [questionType, setQuestionType] = useState<'initial' | 'newVegas' | 'please' | 'thatsit' | 'embrace'>('initial');
  const [showMessage, setShowMessage] = useState('');
  const [showImage, setShowImage] = useState(false)


  const handleTroubleAnswer = (answer: string) => {
    if (answer === 'no') {
      setShowMessage('Then why did you come here?');
    } else if (answer === 'yes') {
      setQuestionType('newVegas');
    }
  };
  const handleNewVegasAnswer = (answer: string) => {
    if (answer === 'yes') {
      setQuestionType('please');
    } else {
      setShowMessage('That\'s too bad. No help for you.');
    }
  };
  const handlePleaseClick = (answer: string) => {
    setQuestionType('thatsit');
    setShowMessage('Okay, just LOCK IN ');
  };
  const handleThatsItAnswer = (answer: string) => {
    if (answer === 'yes') {
      setQuestionType('embrace');
      setShowMessage('You cant go back from here');
    }
  };
  const handleEmbraceAnswer = (answer: string) => {
    if (answer === 'yes') {
      setShowImage(true);
      setQuestionType('embrace');
    }
  };

    return (
    <View style={[styles.container]}>
    {showImage && (
        <Image source={require('./assets/heaven.jpg')} style={styles.largeImage} />
      )}  
      {questionType === 'initial' && (
        <>
          <Text style={styles.questionText}>Are you having trouble with the app?</Text>
          <View style={styles.buttonContainer}>
            <Button title="Yes" onPress={() => handleTroubleAnswer('yes')} />
            <Button title="No" onPress={() => handleTroubleAnswer('no')} />
          </View>
        </>
      )}

      {questionType === 'newVegas' && (
        <>
          <Text style={styles.questionText}>
            Admit that New Vegas is the best game ever made and I'll help you.
          </Text>
          <View style={styles.buttonContainer}>
            <Button title="Fine" onPress={() => handleNewVegasAnswer('yes')} />
          </View>
        </>
      )}

      {questionType === 'please' && (
        <>
          <Text style={styles.questionText}>Only if you say please</Text>
          <View style={styles.buttonContainer}>
            <Button title="Please" onPress={() => handlePleaseClick('yes')} /> 
          </View>
        </>
      )}
      {questionType === 'thatsit' && (
        <>
          <Text style={styles.questionText}>                 Yes, we as humans are defined by our actions. Every choice we make shapes our path, and if you take the path of LOCKING IN, then you are essentially making a choice to move further, learn, grow, progress. What happens next isn't fate, you must decide to embrace it.</Text>
          <View style={styles.buttonContainer}>
            <Button title="Thats it!?!?" onPress={() => handleThatsItAnswer('yes')} />
          </View>
        </>
      )}
      {questionType === 'embrace' && (
        <>
          <View style={styles.buttonContainer}>
            <Button title="Embrace" onPress={() => handleEmbraceAnswer('yes')} />
          </View>
        </>
      )}


      {showMessage && <Text style={styles.messageText}>{showMessage}</Text>}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  
  questionText: {
    fontSize: 18,
    marginBottom: 20,
  },
  messageText: {
    fontSize: 18,
    marginTop: 20,
    fontWeight: 'bold',
     textAlign: 'center',
  },
  buttonContainer: {
    flexDirection: 'row',  
    gap: 10,               
    justifyContent: 'center',
    marginTop: 20,
  },
  largeImage: {
    width: '100%', 
    height: '100%', 
    resizeMode: 'cover', 
  },
});

export default FAQ;
