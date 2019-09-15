import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './teaching-staff.reducer';
import { ITeachingStaff } from 'app/shared/model/teaching-staff.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITeachingStaffProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class TeachingStaff extends React.Component<ITeachingStaffProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { teachingStaffList, match } = this.props;
    return (
      <div>
        <h2 id="teaching-staff-heading">
          <Translate contentKey="catalogApp.teachingStaff.home.title">Teaching Staffs</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.teachingStaff.home.createLabel">Create a new Teaching Staff</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {teachingStaffList && teachingStaffList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingStaff.guid">Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingStaff.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingStaff.graduationType">Graduation Type</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingStaff.paper">Paper</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {teachingStaffList.map((teachingStaff, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${teachingStaff.id}`} color="link" size="sm">
                        {teachingStaff.id}
                      </Button>
                    </td>
                    <td>{teachingStaff.guid}</td>
                    <td>{teachingStaff.name}</td>
                    <td>
                      <Translate contentKey={`catalogApp.GraduationType.${teachingStaff.graduationType}`} />
                    </td>
                    <td>
                      {teachingStaff.papers
                        ? teachingStaff.papers.map((val, j) => (
                            <span key={j}>
                              <Link to={`paper/${val.id}`}>{val.id}</Link>
                              {j === teachingStaff.papers.length - 1 ? '' : ', '}
                            </span>
                          ))
                        : null}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${teachingStaff.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${teachingStaff.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${teachingStaff.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="catalogApp.teachingStaff.home.notFound">No Teaching Staffs found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ teachingStaff }: IRootState) => ({
  teachingStaffList: teachingStaff.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TeachingStaff);
