import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './teaching-class.reducer';
import { ITeachingClass } from 'app/shared/model/teaching-class.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITeachingClassProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class TeachingClass extends React.Component<ITeachingClassProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { teachingClassList, match } = this.props;
    return (
      <div>
        <h2 id="teaching-class-heading">
          <Translate contentKey="catalogApp.teachingClass.home.title">Teaching Classes</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.teachingClass.home.createLabel">Create a new Teaching Class</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {teachingClassList && teachingClassList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingClass.guid">Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingClass.code">Code</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingClass.year">Year</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingClass.semester">Semester</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingClass.campus">Campus</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.teachingClass.paper">Paper</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {teachingClassList.map((teachingClass, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${teachingClass.id}`} color="link" size="sm">
                        {teachingClass.id}
                      </Button>
                    </td>
                    <td>{teachingClass.guid}</td>
                    <td>{teachingClass.code}</td>
                    <td>{teachingClass.year}</td>
                    <td>{teachingClass.semester}</td>
                    <td>{teachingClass.campus ? <Link to={`campus/${teachingClass.campus.id}`}>{teachingClass.campus.guid}</Link> : ''}</td>
                    <td>{teachingClass.paper ? <Link to={`paper/${teachingClass.paper.id}`}>{teachingClass.paper.code}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${teachingClass.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${teachingClass.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${teachingClass.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="catalogApp.teachingClass.home.notFound">No Teaching Classes found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ teachingClass }: IRootState) => ({
  teachingClassList: teachingClass.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TeachingClass);
