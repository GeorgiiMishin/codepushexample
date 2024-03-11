import {useCallback, useMemo, useState} from 'react';
import {inRange} from 'lodash';

type Todo = {
  userId: string;
  id: string;
  title: string;
  completed: boolean;
};

type Idle = {type: 'idle'};
type Loading = {type: 'loading'};
type Ready = {type: 'ready'; data: Todo[]};
type Failure = {type: 'failure'; error: unknown};

type TodoState = Idle | Loading | Ready | Failure;

type Feature = {
  state: TodoState;
  load(): void;
};

async function loadTodos() {
  const response = await fetch('https://jsonplaceholder.typicode.com/todos');

  if (!inRange(response.status, 200, 300)) {
    throw new Error(`Http status: ${response.status}`);
  }

  const result: Todo[] = await response.json();

  return result;
}

export const useTodos = (): Feature => {
  const [state, setState] = useState<TodoState>({type: 'idle'});

  const load = useCallback(() => {
    if (state.type === 'loading') {
      return;
    }

    setState({type: 'loading'});

    loadTodos()
      .then(todos => {
        setState({type: 'ready', data: todos});
      })
      .catch((ex: unknown) => {
        setState({type: 'failure', error: ex});
      });
  }, [state]);

  return useMemo(() => ({load, state}), [load, state]);
};
