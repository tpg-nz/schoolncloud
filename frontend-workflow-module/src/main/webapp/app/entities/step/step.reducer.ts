import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStep, defaultValue } from 'app/shared/model/step.model';

export const ACTION_TYPES = {
  FETCH_STEP_LIST: 'step/FETCH_STEP_LIST',
  FETCH_STEP: 'step/FETCH_STEP',
  CREATE_STEP: 'step/CREATE_STEP',
  UPDATE_STEP: 'step/UPDATE_STEP',
  DELETE_STEP: 'step/DELETE_STEP',
  RESET: 'step/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStep>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type StepState = Readonly<typeof initialState>;

// Reducer

export default (state: StepState = initialState, action): StepState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STEP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STEP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STEP):
    case REQUEST(ACTION_TYPES.UPDATE_STEP):
    case REQUEST(ACTION_TYPES.DELETE_STEP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_STEP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STEP):
    case FAILURE(ACTION_TYPES.CREATE_STEP):
    case FAILURE(ACTION_TYPES.UPDATE_STEP):
    case FAILURE(ACTION_TYPES.DELETE_STEP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_STEP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_STEP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STEP):
    case SUCCESS(ACTION_TYPES.UPDATE_STEP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STEP):
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

const apiUrl = 'api/steps';

// Actions

export const getEntities: ICrudGetAllAction<IStep> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_STEP_LIST,
    payload: axios.get<IStep>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IStep> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STEP,
    payload: axios.get<IStep>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStep> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STEP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStep> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STEP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStep> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STEP,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
