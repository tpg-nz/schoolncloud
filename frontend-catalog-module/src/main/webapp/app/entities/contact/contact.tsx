import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './contact.reducer';
import { IContact } from 'app/shared/model/contact.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Contact extends React.Component<IContactProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { contactList, match } = this.props;
    return (
      <div>
        <h2 id="contact-heading">
          <Translate contentKey="catalogApp.contact.home.title">Contacts</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.contact.home.createLabel">Create a new Contact</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {contactList && contactList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.contact.guid">Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.contact.entityGuid">Entity Guid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.contact.contact">Contact</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.contact.contactType">Contact Type</Translate>
                  </th>
                  <th>
                    <Translate contentKey="catalogApp.contact.mediaType">Media Type</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {contactList.map((contact, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${contact.id}`} color="link" size="sm">
                        {contact.id}
                      </Button>
                    </td>
                    <td>{contact.guid}</td>
                    <td>{contact.entityGuid}</td>
                    <td>{contact.contact}</td>
                    <td>
                      <Translate contentKey={`catalogApp.ContactType.${contact.contactType}`} />
                    </td>
                    <td>
                      <Translate contentKey={`catalogApp.MediaTypee.${contact.mediaType}`} />
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${contact.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${contact.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${contact.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="catalogApp.contact.home.notFound">No Contacts found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ contact }: IRootState) => ({
  contactList: contact.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Contact);
