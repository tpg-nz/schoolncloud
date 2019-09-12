import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IWorkflow, defaultValue } from 'app/shared/model/workflow.model';

export const ACTION_TYPES = {
  FETCH_WORKFLOW_LIST: 'workflow/FETCH_WORKFLOW_LIST',
  FETCH_WORKFLOW: 'workflow/FETCH_WORKFLOW',
  CREATE_WORKFLOW: 'workflow/CREATE_WORKFLOW',
  UPDATE_WORKFLOW: 'workflow/UPDATE_WORKFLOW',
  DELETE_WORKFLOW: 'workflow/DELETE_WORKFLOW',
  RESET: 'workflow/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IWorkflow>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type WorkflowState = Readonly<typeof initialState>;

// Reducer

export default (state: WorkflowState = initialState, action): WorkflowState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_WORKFLOW_LIST):
    case REQUEST(ACTION_TYPES.FETCH_WORKFLOW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_WORKFLOW):
    case REQUEST(ACTION_TYPES.UPDATE_WORKFLOW):
    case REQUEST(ACTION_TYPES.DELETE_WORKFLOW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_WORKFLOW_LIST):
    case FAILURE(ACTION_TYPES.FETCH_WORKFLOW):
    case FAILURE(ACTION_TYPES.CREATE_WORKFLOW):
    case FAILURE(ACTION_TYPES.UPDATE_WORKFLOW):
    case FAILURE(ACTION_TYPES.DELETE_WORKFLOW):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_WORKFLOW_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_WORKFLOW):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_WORKFLOW):
    case SUCCESS(ACTION_TYPES.UPDATE_WORKFLOW):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_WORKFLOW):
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

const apiUrl = 'api/workflows';

// Actions

export const getEntities: ICrudGetAllAction<IWorkflow> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_WORKFLOW_LIST,
    payload: axios.get<IWorkflow>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IWorkflow> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_WORKFLOW,
    payload: axios.get<IWorkflow>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IWorkflow> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_WORKFLOW,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IWorkflow> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_WORKFLOW,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IWorkflow> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_WORKFLOW,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
