import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEducationalInstitution, defaultValue } from 'app/shared/model/educational-institution.model';

export const ACTION_TYPES = {
  FETCH_EDUCATIONALINSTITUTION_LIST: 'educationalInstitution/FETCH_EDUCATIONALINSTITUTION_LIST',
  FETCH_EDUCATIONALINSTITUTION: 'educationalInstitution/FETCH_EDUCATIONALINSTITUTION',
  CREATE_EDUCATIONALINSTITUTION: 'educationalInstitution/CREATE_EDUCATIONALINSTITUTION',
  UPDATE_EDUCATIONALINSTITUTION: 'educationalInstitution/UPDATE_EDUCATIONALINSTITUTION',
  DELETE_EDUCATIONALINSTITUTION: 'educationalInstitution/DELETE_EDUCATIONALINSTITUTION',
  RESET: 'educationalInstitution/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEducationalInstitution>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type EducationalInstitutionState = Readonly<typeof initialState>;

// Reducer

export default (state: EducationalInstitutionState = initialState, action): EducationalInstitutionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EDUCATIONALINSTITUTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EDUCATIONALINSTITUTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EDUCATIONALINSTITUTION):
    case REQUEST(ACTION_TYPES.UPDATE_EDUCATIONALINSTITUTION):
    case REQUEST(ACTION_TYPES.DELETE_EDUCATIONALINSTITUTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EDUCATIONALINSTITUTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EDUCATIONALINSTITUTION):
    case FAILURE(ACTION_TYPES.CREATE_EDUCATIONALINSTITUTION):
    case FAILURE(ACTION_TYPES.UPDATE_EDUCATIONALINSTITUTION):
    case FAILURE(ACTION_TYPES.DELETE_EDUCATIONALINSTITUTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EDUCATIONALINSTITUTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_EDUCATIONALINSTITUTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EDUCATIONALINSTITUTION):
    case SUCCESS(ACTION_TYPES.UPDATE_EDUCATIONALINSTITUTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EDUCATIONALINSTITUTION):
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

const apiUrl = 'api/educational-institutions';

// Actions

export const getEntities: ICrudGetAllAction<IEducationalInstitution> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EDUCATIONALINSTITUTION_LIST,
    payload: axios.get<IEducationalInstitution>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IEducationalInstitution> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EDUCATIONALINSTITUTION,
    payload: axios.get<IEducationalInstitution>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEducationalInstitution> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EDUCATIONALINSTITUTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEducationalInstitution> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EDUCATIONALINSTITUTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEducationalInstitution> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EDUCATIONALINSTITUTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
