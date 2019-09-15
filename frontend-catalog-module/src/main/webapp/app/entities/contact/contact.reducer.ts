import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IContact, defaultValue } from 'app/shared/model/contact.model';

export const ACTION_TYPES = {
  FETCH_CONTACT_LIST: 'contact/FETCH_CONTACT_LIST',
  FETCH_CONTACT: 'contact/FETCH_CONTACT',
  CREATE_CONTACT: 'contact/CREATE_CONTACT',
  UPDATE_CONTACT: 'contact/UPDATE_CONTACT',
  DELETE_CONTACT: 'contact/DELETE_CONTACT',
  RESET: 'contact/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IContact>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ContactState = Readonly<typeof initialState>;

// Reducer

export default (state: ContactState = initialState, action): ContactState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CONTACT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONTACT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONTACT):
    case REQUEST(ACTION_TYPES.UPDATE_CONTACT):
    case REQUEST(ACTION_TYPES.DELETE_CONTACT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CONTACT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONTACT):
    case FAILURE(ACTION_TYPES.CREATE_CONTACT):
    case FAILURE(ACTION_TYPES.UPDATE_CONTACT):
    case FAILURE(ACTION_TYPES.DELETE_CONTACT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTACT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTACT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONTACT):
    case SUCCESS(ACTION_TYPES.UPDATE_CONTACT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONTACT):
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

const apiUrl = 'api/contacts';

// Actions

export const getEntities: ICrudGetAllAction<IContact> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CONTACT_LIST,
  payload: axios.get<IContact>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IContact> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONTACT,
    payload: axios.get<IContact>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IContact> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONTACT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IContact> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONTACT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IContact> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONTACT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
