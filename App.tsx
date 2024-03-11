/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, {useEffect} from 'react';
import {NativeModules, Platform} from 'react-native';
import {TodoApp} from './src';

const {CodePushModule} = NativeModules;

function App(): React.JSX.Element {
  useEffect(() => {
    CodePushModule.initialize(
      Platform.select({
        default: 'http://localhost:3000',
        android: 'http://10.0.2.2:3000',
      }),
    );
  }, []);

  return <TodoApp />;
}

export default App;
