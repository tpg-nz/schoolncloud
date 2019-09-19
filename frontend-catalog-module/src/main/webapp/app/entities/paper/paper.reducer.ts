import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPaper, defaultValue } from 'app/shared/model/paper.model';

export const ACTION_TYPES = {
  FETCH_PAPER_LIST: 'paper/FETCH_PAPER_LIST',
  FETCH_PAPER: 'paper/FETCH_PAPER',
  CREATE_PAPER: 'paper/CREATE_PAPER',
  UPDATE_PAPER: 'paper/UPDATE_PAPER',
  DELETE_PAPER: 'paper/DELETE_PAPER',
  RESET: 'paper/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPaper>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PaperState = Readonly<typeof initialState>;

// Reducer

export default (state: PaperState = initialState, action): PaperState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PAPER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PAPER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PAPER):
    case REQUEST(ACTION_TYPES.UPDATE_PAPER):
    case REQUEST(ACTION_TYPES.DELETE_PAPER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PAPER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PAPER):
    case FAILURE(ACTION_TYPES.CREATE_PAPER):
    case FAILURE(ACTION_TYPES.UPDATE_PAPER):
    case FAILURE(ACTION_TYPES.DELETE_PAPER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAPER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAPER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PAPER):
    case SUCCESS(ACTION_TYPES.UPDATE_PAPER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PAPER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/papers';

// Actions

export const getEntities: ICrudGetAllAction<IPaper> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PAPER_LIST,
    payload: axios.get<IPaper>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPaper> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PAPER,
    payload: axios.get<IPaper>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPaper> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PAPER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPaper> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAPER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPaper> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PAPER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
