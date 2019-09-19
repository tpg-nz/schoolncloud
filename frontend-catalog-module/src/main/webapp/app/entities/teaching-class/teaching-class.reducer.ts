import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITeachingClass, defaultValue } from 'app/shared/model/teaching-class.model';

export const ACTION_TYPES = {
  FETCH_TEACHINGCLASS_LIST: 'teachingClass/FETCH_TEACHINGCLASS_LIST',
  FETCH_TEACHINGCLASS: 'teachingClass/FETCH_TEACHINGCLASS',
  CREATE_TEACHINGCLASS: 'teachingClass/CREATE_TEACHINGCLASS',
  UPDATE_TEACHINGCLASS: 'teachingClass/UPDATE_TEACHINGCLASS',
  DELETE_TEACHINGCLASS: 'teachingClass/DELETE_TEACHINGCLASS',
  RESET: 'teachingClass/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITeachingClass>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type TeachingClassState = Readonly<typeof initialState>;

// Reducer

export default (state: TeachingClassState = initialState, action): TeachingClassState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TEACHINGCLASS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEACHINGCLASS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEACHINGCLASS):
    case REQUEST(ACTION_TYPES.UPDATE_TEACHINGCLASS):
    case REQUEST(ACTION_TYPES.DELETE_TEACHINGCLASS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TEACHINGCLASS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEACHINGCLASS):
    case FAILURE(ACTION_TYPES.CREATE_TEACHINGCLASS):
    case FAILURE(ACTION_TYPES.UPDATE_TEACHINGCLASS):
    case FAILURE(ACTION_TYPES.DELETE_TEACHINGCLASS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEACHINGCLASS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEACHINGCLASS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEACHINGCLASS):
    case SUCCESS(ACTION_TYPES.UPDATE_TEACHINGCLASS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEACHINGCLASS):
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

const apiUrl = 'api/teaching-classes';

// Actions

export const getEntities: ICrudGetAllAction<ITeachingClass> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TEACHINGCLASS_LIST,
    payload: axios.get<ITeachingClass>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ITeachingClass> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEACHINGCLASS,
    payload: axios.get<ITeachingClass>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITeachingClass> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEACHINGCLASS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITeachingClass> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEACHINGCLASS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITeachingClass> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEACHINGCLASS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
