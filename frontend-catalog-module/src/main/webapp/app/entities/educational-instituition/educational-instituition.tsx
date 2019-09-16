import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './educational-instituition.reducer';
import { IEducationalInstituition } from 'app/shared/model/educational-instituition.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEducationalInstituitionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class EducationalInstituition extends React.Component<IEducationalInstituitionProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { educationalInstituitionList, match } = this.props;
    return (
      <div>
        <h2 id="educational-instituition-heading">
          <Translate contentKey="catalogApp.educationalInstituition.home.title">Educational Instituitions</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.educationalInstituition.home.createLabel">Create new Educational Instituition</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {educationalInstituitionList && educationalInstituitionList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.educationalInstituition.guid">Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.educationalInstituition.name">Name</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {educationalInstituitionList.map((educationalInstituition, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${educationalInstituition.id}`} color="link" size="sm">
                        {educationalInstituition.id}
                      </Button>
                    </td>
                    <td>{educationalInstituition.guid}</td>
                    <td>{educationalInstituition.name}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${educationalInstituition.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${educationalInstituition.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${educationalInstituition.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="catalogApp.educationalInstituition.home.notFound">No Educational Instituitions found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ educationalInstituition }: IRootState) => ({
  educationalInstituitionList: educationalInstituition.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EducationalInstituition);
