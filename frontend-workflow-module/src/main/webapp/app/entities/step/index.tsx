import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Step from './step';
import StepDetail from './step-detail';
import StepUpdate from './step-update';
import StepDeleteDialog from './step-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StepUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StepUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StepDetail} />
      <ErrorBoundaryRoute path={match.url} component={Step} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={StepDeleteDialog} />
  </>
);

export default Routes;
