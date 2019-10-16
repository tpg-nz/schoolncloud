import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './step.reducer';
import { IStep } from 'app/shared/model/step.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStepDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class StepDetail extends React.Component<IStepDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { stepEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="workflowApp.step.detail.title">Step</Translate> [<b>{stepEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="sequence">
                <Translate contentKey="workflowApp.step.sequence">Sequence</Translate>
              </span>
            </dt>
            <dd>{stepEntity.sequence}</dd>
            <dt>
              <span id="name">
                <Translate contentKey="workflowApp.step.name">Name</Translate>
              </span>
            </dt>
            <dd>{stepEntity.name}</dd>
            <dt>
              <Translate contentKey="workflowApp.step.workflow">Workflow</Translate>
            </dt>
            <dd>{stepEntity.workflow ? stepEntity.workflow.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/step" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/step/${stepEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ step }: IRootState) => ({
  stepEntity: step.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StepDetail);
