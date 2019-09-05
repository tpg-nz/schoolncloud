import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Workflow from './workflow';
import WorkflowDetail from './workflow-detail';
import WorkflowUpdate from './workflow-update';
import WorkflowDeleteDialog from './workflow-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkflowUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkflowUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkflowDetail} />
      <ErrorBoundaryRoute path={match.url} component={Workflow} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={WorkflowDeleteDialog} />
  </>
);

export default Routes;
