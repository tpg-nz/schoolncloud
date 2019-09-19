import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IQualification, defaultValue } from 'app/shared/model/qualification.model';

export const ACTION_TYPES = {
  FETCH_QUALIFICATION_LIST: 'qualification/FETCH_QUALIFICATION_LIST',
  FETCH_QUALIFICATION: 'qualification/FETCH_QUALIFICATION',
  CREATE_QUALIFICATION: 'qualification/CREATE_QUALIFICATION',
  UPDATE_QUALIFICATION: 'qualification/UPDATE_QUALIFICATION',
  DELETE_QUALIFICATION: 'qualification/DELETE_QUALIFICATION',
  RESET: 'qualification/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IQualification>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type QualificationState = Readonly<typeof initialState>;

// Reducer

export default (state: QualificationState = initialState, action): QualificationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_QUALIFICATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_QUALIFICATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_QUALIFICATION):
    case REQUEST(ACTION_TYPES.UPDATE_QUALIFICATION):
    case REQUEST(ACTION_TYPES.DELETE_QUALIFICATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_QUALIFICATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_QUALIFICATION):
    case FAILURE(ACTION_TYPES.CREATE_QUALIFICATION):
    case FAILURE(ACTION_TYPES.UPDATE_QUALIFICATION):
    case FAILURE(ACTION_TYPES.DELETE_QUALIFICATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_QUALIFICATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_QUALIFICATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_QUALIFICATION):
    case SUCCESS(ACTION_TYPES.UPDATE_QUALIFICATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_QUALIFICATION):
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

const apiUrl = 'api/qualifications';

// Actions

export const getEntities: ICrudGetAllAction<IQualification> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_QUALIFICATION_LIST,
    payload: axios.get<IQualification>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IQualification> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_QUALIFICATION,
    payload: axios.get<IQualification>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IQualification> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_QUALIFICATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IQualification> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_QUALIFICATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IQualification> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_QUALIFICATION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
