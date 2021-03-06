import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './qualification.reducer';
import { IQualification } from 'app/shared/model/qualification.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IQualificationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class QualificationDetail extends React.Component<IQualificationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { qualificationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="catalogApp.qualification.detail.title">Qualification</Translate> [<b>{qualificationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="catalogApp.qualification.name">Name</Translate>
              </span>
            </dt>
            <dd>{qualificationEntity.name}</dd>
            <dt>
              <span id="hyperLink">
                <Translate contentKey="catalogApp.qualification.hyperLink">Hyper Link</Translate>
              </span>
            </dt>
            <dd>{qualificationEntity.hyperLink}</dd>
            <dt>
              <Translate contentKey="catalogApp.qualification.subject">Subject</Translate>
            </dt>
            <dd>{qualificationEntity.subject ? qualificationEntity.subject.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/qualification" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/qualification/${qualificationEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ qualification }: IRootState) => ({
  qualificationEntity: qualification.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(QualificationDetail);
