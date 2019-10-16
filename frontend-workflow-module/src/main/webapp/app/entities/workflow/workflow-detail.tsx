import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './workflow.reducer';
import { IWorkflow } from 'app/shared/model/workflow.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IWorkflowDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class WorkflowDetail extends React.Component<IWorkflowDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { workflowEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="workflowApp.workflow.detail.title">Workflow</Translate> [<b>{workflowEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="workflowApp.workflow.name">Name</Translate>
              </span>
            </dt>
            <dd>{workflowEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="workflowApp.workflow.description">Description</Translate>
              </span>
            </dt>
            <dd>{workflowEntity.description}</dd>
            <dt>
              <span id="enabled">
                <Translate contentKey="workflowApp.workflow.enabled">Enabled</Translate>
              </span>
            </dt>
            <dd>{workflowEntity.enabled ? 'true' : 'false'}</dd>
            <dt>
              <span id="version">
                <Translate contentKey="workflowApp.workflow.version">Version</Translate>
              </span>
            </dt>
            <dd>{workflowEntity.version}</dd>
            <dt>
              <Translate contentKey="workflowApp.workflow.workflow">Workflow</Translate>
            </dt>
            <dd>{workflowEntity.workflow ? workflowEntity.workflow.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/workflow" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/workflow/${workflowEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ workflow }: IRootState) => ({
  workflowEntity: workflow.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorkflowDetail);
