import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './role.reducer';
import { IRole } from 'app/shared/model/role.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRoleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Role extends React.Component<IRoleProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { roleList, match } = this.props;
    return (
      <div>
        <h2 id="role-heading">
          <Translate contentKey="catalogApp.role.home.title">Roles</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.role.home.createLabel">Create new Role</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {roleList && roleList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.role.guid">Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.role.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.role.description">Description</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {roleList.map((role, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${role.id}`} color="link" size="sm">
                        {role.id}
                      </Button>
                    </td>
                    <td>{role.guid}</td>
                    <td>{role.name}</td>
                    <td>{role.description}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${role.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${role.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${role.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="catalogApp.role.home.notFound">No Roles found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ role }: IRootState) => ({
  roleList: role.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Role);
