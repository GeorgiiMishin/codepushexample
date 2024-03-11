import React from 'react';
import {
  Button,
  FlatList,
  SafeAreaView,
  ActivityIndicator,
  Text,
  StyleSheet,
  View,
} from 'react-native';
import {useTodos} from './useTodos';

export const TodoApp = () => {
  const {state, load} = useTodos();

  return (
    <SafeAreaView style={styles.container}>
      {state.type === 'idle' && <Button title="Загрузить" onPress={load} />}
      {state.type === 'loading' && <ActivityIndicator />}
      {state.type === 'failure' && <Text>Ошибка загрузки данных.</Text>}
      {state.type === 'ready' && (
        <FlatList
          data={state.data}
          renderItem={({item, index}) => (
            <View style={styles.itemContainer}>
              <Text>
                {index + 1}. {item.title}
              </Text>
            </View>
          )}
        />
      )}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  itemContainer: {
    padding: 10,
    borderBottomColor: 'black',
    borderBottomWidth: 0.5,
  },
});
