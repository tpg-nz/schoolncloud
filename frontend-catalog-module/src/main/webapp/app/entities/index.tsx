import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Subject from './subject';
import Paper from './paper';
import Requirement from './requirement';
import Qualification from './qualification';
import TeachingStaff from './teaching-staff';
import TeachingClass from './teaching-class';
import EducationalInstitution from './educational-institution';
import Campus from './campus';
import Contact from './contact';
import Address from './address';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/subject`} component={Subject} />
      <ErrorBoundaryRoute path={`${match.url}/paper`} component={Paper} />
      <ErrorBoundaryRoute path={`${match.url}/requirement`} component={Requirement} />
      <ErrorBoundaryRoute path={`${match.url}/qualification`} component={Qualification} />
      <ErrorBoundaryRoute path={`${match.url}/teaching-staff`} component={TeachingStaff} />
      <ErrorBoundaryRoute path={`${match.url}/teaching-class`} component={TeachingClass} />
      <ErrorBoundaryRoute path={`${match.url}/educational-institution`} component={EducationalInstitution} />
      <ErrorBoundaryRoute path={`${match.url}/campus`} component={Campus} />
      <ErrorBoundaryRoute path={`${match.url}/contact`} component={Contact} />
      <ErrorBoundaryRoute path={`${match.url}/address`} component={Address} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
