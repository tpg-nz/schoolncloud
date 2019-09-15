import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './teaching-staff.reducer';
import { ITeachingStaff } from 'app/shared/model/teaching-staff.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITeachingStaffDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TeachingStaffDetail extends React.Component<ITeachingStaffDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { teachingStaffEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="catalogApp.teachingStaff.detail.title">TeachingStaff</Translate> [<b>{teachingStaffEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guid">
                <Translate contentKey="catalogApp.teachingStaff.guid">Guid</Translate>
              </span>
            </dt>
            <dd>{teachingStaffEntity.guid}</dd>
            <dt>
              <span id="name">
                <Translate contentKey="catalogApp.teachingStaff.name">Name</Translate>
              </span>
            </dt>
            <dd>{teachingStaffEntity.name}</dd>
            <dt>
              <span id="graduationType">
                <Translate contentKey="catalogApp.teachingStaff.graduationType">Graduation Type</Translate>
              </span>
            </dt>
            <dd>{teachingStaffEntity.graduationType}</dd>
            <dt>
              <Translate contentKey="catalogApp.teachingStaff.paper">Paper</Translate>
            </dt>
            <dd>
              {teachingStaffEntity.papers
                ? teachingStaffEntity.papers.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.id}</a>
                      {i === teachingStaffEntity.papers.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/teaching-staff" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/teaching-staff/${teachingStaffEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ teachingStaff }: IRootState) => ({
  teachingStaffEntity: teachingStaff.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TeachingStaffDetail);
