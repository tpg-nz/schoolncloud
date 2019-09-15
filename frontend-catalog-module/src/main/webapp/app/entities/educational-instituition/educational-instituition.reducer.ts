import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEducationalInstituition, defaultValue } from 'app/shared/model/educational-instituition.model';

export const ACTION_TYPES = {
  FETCH_EDUCATIONALINSTITUITION_LIST: 'educationalInstituition/FETCH_EDUCATIONALINSTITUITION_LIST',
  FETCH_EDUCATIONALINSTITUITION: 'educationalInstituition/FETCH_EDUCATIONALINSTITUITION',
  CREATE_EDUCATIONALINSTITUITION: 'educationalInstituition/CREATE_EDUCATIONALINSTITUITION',
  UPDATE_EDUCATIONALINSTITUITION: 'educationalInstituition/UPDATE_EDUCATIONALINSTITUITION',
  DELETE_EDUCATIONALINSTITUITION: 'educationalInstituition/DELETE_EDUCATIONALINSTITUITION',
  RESET: 'educationalInstituition/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEducationalInstituition>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type EducationalInstituitionState = Readonly<typeof initialState>;

// Reducer

export default (state: EducationalInstituitionState = initialState, action): EducationalInstituitionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EDUCATIONALINSTITUITION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EDUCATIONALINSTITUITION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EDUCATIONALINSTITUITION):
    case REQUEST(ACTION_TYPES.UPDATE_EDUCATIONALINSTITUITION):
    case REQUEST(ACTION_TYPES.DELETE_EDUCATIONALINSTITUITION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EDUCATIONALINSTITUITION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EDUCATIONALINSTITUITION):
    case FAILURE(ACTION_TYPES.CREATE_EDUCATIONALINSTITUITION):
    case FAILURE(ACTION_TYPES.UPDATE_EDUCATIONALINSTITUITION):
    case FAILURE(ACTION_TYPES.DELETE_EDUCATIONALINSTITUITION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EDUCATIONALINSTITUITION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EDUCATIONALINSTITUITION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EDUCATIONALINSTITUITION):
    case SUCCESS(ACTION_TYPES.UPDATE_EDUCATIONALINSTITUITION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EDUCATIONALINSTITUITION):
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

const apiUrl = 'api/educational-instituitions';

// Actions

export const getEntities: ICrudGetAllAction<IEducationalInstituition> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_EDUCATIONALINSTITUITION_LIST,
  payload: axios.get<IEducationalInstituition>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IEducationalInstituition> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EDUCATIONALINSTITUITION,
    payload: axios.get<IEducationalInstituition>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEducationalInstituition> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EDUCATIONALINSTITUITION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEducationalInstituition> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EDUCATIONALINSTITUITION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEducationalInstituition> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EDUCATIONALINSTITUITION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
