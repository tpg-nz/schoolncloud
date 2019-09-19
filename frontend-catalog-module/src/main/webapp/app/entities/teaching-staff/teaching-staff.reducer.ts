import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITeachingStaff, defaultValue } from 'app/shared/model/teaching-staff.model';

export const ACTION_TYPES = {
  FETCH_TEACHINGSTAFF_LIST: 'teachingStaff/FETCH_TEACHINGSTAFF_LIST',
  FETCH_TEACHINGSTAFF: 'teachingStaff/FETCH_TEACHINGSTAFF',
  CREATE_TEACHINGSTAFF: 'teachingStaff/CREATE_TEACHINGSTAFF',
  UPDATE_TEACHINGSTAFF: 'teachingStaff/UPDATE_TEACHINGSTAFF',
  DELETE_TEACHINGSTAFF: 'teachingStaff/DELETE_TEACHINGSTAFF',
  RESET: 'teachingStaff/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITeachingStaff>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type TeachingStaffState = Readonly<typeof initialState>;

// Reducer

export default (state: TeachingStaffState = initialState, action): TeachingStaffState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TEACHINGSTAFF_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEACHINGSTAFF):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEACHINGSTAFF):
    case REQUEST(ACTION_TYPES.UPDATE_TEACHINGSTAFF):
    case REQUEST(ACTION_TYPES.DELETE_TEACHINGSTAFF):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TEACHINGSTAFF_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEACHINGSTAFF):
    case FAILURE(ACTION_TYPES.CREATE_TEACHINGSTAFF):
    case FAILURE(ACTION_TYPES.UPDATE_TEACHINGSTAFF):
    case FAILURE(ACTION_TYPES.DELETE_TEACHINGSTAFF):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEACHINGSTAFF_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEACHINGSTAFF):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEACHINGSTAFF):
    case SUCCESS(ACTION_TYPES.UPDATE_TEACHINGSTAFF):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEACHINGSTAFF):
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

const apiUrl = 'api/teaching-staffs';

// Actions

export const getEntities: ICrudGetAllAction<ITeachingStaff> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TEACHINGSTAFF_LIST,
    payload: axios.get<ITeachingStaff>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ITeachingStaff> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEACHINGSTAFF,
    payload: axios.get<ITeachingStaff>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITeachingStaff> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEACHINGSTAFF,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITeachingStaff> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEACHINGSTAFF,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITeachingStaff> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEACHINGSTAFF,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
