import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStepField, defaultValue } from 'app/shared/model/step-field.model';

export const ACTION_TYPES = {
  FETCH_STEPFIELD_LIST: 'stepField/FETCH_STEPFIELD_LIST',
  FETCH_STEPFIELD: 'stepField/FETCH_STEPFIELD',
  CREATE_STEPFIELD: 'stepField/CREATE_STEPFIELD',
  UPDATE_STEPFIELD: 'stepField/UPDATE_STEPFIELD',
  DELETE_STEPFIELD: 'stepField/DELETE_STEPFIELD',
  RESET: 'stepField/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStepField>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type StepFieldState = Readonly<typeof initialState>;

// Reducer

export default (state: StepFieldState = initialState, action): StepFieldState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STEPFIELD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STEPFIELD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STEPFIELD):
    case REQUEST(ACTION_TYPES.UPDATE_STEPFIELD):
    case REQUEST(ACTION_TYPES.DELETE_STEPFIELD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_STEPFIELD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STEPFIELD):
    case FAILURE(ACTION_TYPES.CREATE_STEPFIELD):
    case FAILURE(ACTION_TYPES.UPDATE_STEPFIELD):
    case FAILURE(ACTION_TYPES.DELETE_STEPFIELD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_STEPFIELD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_STEPFIELD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STEPFIELD):
    case SUCCESS(ACTION_TYPES.UPDATE_STEPFIELD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STEPFIELD):
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

const apiUrl = 'api/step-fields';

// Actions

export const getEntities: ICrudGetAllAction<IStepField> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_STEPFIELD_LIST,
    payload: axios.get<IStepField>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IStepField> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STEPFIELD,
    payload: axios.get<IStepField>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStepField> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STEPFIELD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStepField> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STEPFIELD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStepField> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STEPFIELD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
