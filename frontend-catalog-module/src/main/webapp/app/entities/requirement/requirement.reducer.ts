import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRequirement, defaultValue } from 'app/shared/model/requirement.model';

export const ACTION_TYPES = {
  FETCH_REQUIREMENT_LIST: 'requirement/FETCH_REQUIREMENT_LIST',
  FETCH_REQUIREMENT: 'requirement/FETCH_REQUIREMENT',
  CREATE_REQUIREMENT: 'requirement/CREATE_REQUIREMENT',
  UPDATE_REQUIREMENT: 'requirement/UPDATE_REQUIREMENT',
  DELETE_REQUIREMENT: 'requirement/DELETE_REQUIREMENT',
  RESET: 'requirement/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRequirement>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RequirementState = Readonly<typeof initialState>;

// Reducer

export default (state: RequirementState = initialState, action): RequirementState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_REQUIREMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REQUIREMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_REQUIREMENT):
    case REQUEST(ACTION_TYPES.UPDATE_REQUIREMENT):
    case REQUEST(ACTION_TYPES.DELETE_REQUIREMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_REQUIREMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_REQUIREMENT):
    case FAILURE(ACTION_TYPES.CREATE_REQUIREMENT):
    case FAILURE(ACTION_TYPES.UPDATE_REQUIREMENT):
    case FAILURE(ACTION_TYPES.DELETE_REQUIREMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_REQUIREMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_REQUIREMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_REQUIREMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_REQUIREMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_REQUIREMENT):
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

const apiUrl = 'api/requirements';

// Actions

export const getEntities: ICrudGetAllAction<IRequirement> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_REQUIREMENT_LIST,
  payload: axios.get<IRequirement>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRequirement> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_REQUIREMENT,
    payload: axios.get<IRequirement>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRequirement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_REQUIREMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRequirement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_REQUIREMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRequirement> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_REQUIREMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
