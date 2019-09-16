import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './campus.reducer';
import { ICampus } from 'app/shared/model/campus.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICampusProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Campus extends React.Component<ICampusProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { campusList, match } = this.props;
    return (
      <div>
        <h2 id="campus-heading">
          <Translate contentKey="catalogApp.campus.home.title">Campuses</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.campus.home.createLabel">Create new Campus</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {campusList && campusList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.campus.guid">Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.campus.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.campus.educationalInstitution">Educational Institution</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {campusList.map((campus, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${campus.id}`} color="link" size="sm">
                        {campus.id}
                      </Button>
                    </td>
                    <td>{campus.guid}</td>
                    <td>{campus.name}</td>
                    <td>
                      {campus.educationalInstitution ? (
                        <Link to={`educational-instituition/${campus.educationalInstitution.id}`}>
                          {campus.educationalInstitution.name}
                        </Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${campus.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${campus.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${campus.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="catalogApp.campus.home.notFound">No Campuses found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ campus }: IRootState) => ({
  campusList: campus.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Campus);
