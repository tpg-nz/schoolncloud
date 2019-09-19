import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICampus, defaultValue } from 'app/shared/model/campus.model';

export const ACTION_TYPES = {
  FETCH_CAMPUS_LIST: 'campus/FETCH_CAMPUS_LIST',
  FETCH_CAMPUS: 'campus/FETCH_CAMPUS',
  CREATE_CAMPUS: 'campus/CREATE_CAMPUS',
  UPDATE_CAMPUS: 'campus/UPDATE_CAMPUS',
  DELETE_CAMPUS: 'campus/DELETE_CAMPUS',
  RESET: 'campus/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICampus>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CampusState = Readonly<typeof initialState>;

// Reducer

export default (state: CampusState = initialState, action): CampusState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CAMPUS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CAMPUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CAMPUS):
    case REQUEST(ACTION_TYPES.UPDATE_CAMPUS):
    case REQUEST(ACTION_TYPES.DELETE_CAMPUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CAMPUS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CAMPUS):
    case FAILURE(ACTION_TYPES.CREATE_CAMPUS):
    case FAILURE(ACTION_TYPES.UPDATE_CAMPUS):
    case FAILURE(ACTION_TYPES.DELETE_CAMPUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CAMPUS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CAMPUS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CAMPUS):
    case SUCCESS(ACTION_TYPES.UPDATE_CAMPUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CAMPUS):
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

const apiUrl = 'api/campuses';

// Actions

export const getEntities: ICrudGetAllAction<ICampus> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CAMPUS_LIST,
    payload: axios.get<ICampus>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICampus> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CAMPUS,
    payload: axios.get<ICampus>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICampus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CAMPUS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICampus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CAMPUS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICampus> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CAMPUS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
