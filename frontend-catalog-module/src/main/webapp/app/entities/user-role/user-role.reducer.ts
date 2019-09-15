import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserRole, defaultValue } from 'app/shared/model/user-role.model';

export const ACTION_TYPES = {
  FETCH_USERROLE_LIST: 'userRole/FETCH_USERROLE_LIST',
  FETCH_USERROLE: 'userRole/FETCH_USERROLE',
  CREATE_USERROLE: 'userRole/CREATE_USERROLE',
  UPDATE_USERROLE: 'userRole/UPDATE_USERROLE',
  DELETE_USERROLE: 'userRole/DELETE_USERROLE',
  RESET: 'userRole/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserRole>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type UserRoleState = Readonly<typeof initialState>;

// Reducer

export default (state: UserRoleState = initialState, action): UserRoleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERROLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERROLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERROLE):
    case REQUEST(ACTION_TYPES.UPDATE_USERROLE):
    case REQUEST(ACTION_TYPES.DELETE_USERROLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_USERROLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERROLE):
    case FAILURE(ACTION_TYPES.CREATE_USERROLE):
    case FAILURE(ACTION_TYPES.UPDATE_USERROLE):
    case FAILURE(ACTION_TYPES.DELETE_USERROLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERROLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERROLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERROLE):
    case SUCCESS(ACTION_TYPES.UPDATE_USERROLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERROLE):
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

const apiUrl = 'api/user-roles';

// Actions

export const getEntities: ICrudGetAllAction<IUserRole> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_USERROLE_LIST,
  payload: axios.get<IUserRole>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IUserRole> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERROLE,
    payload: axios.get<IUserRole>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserRole> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERROLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserRole> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERROLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserRole> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERROLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
