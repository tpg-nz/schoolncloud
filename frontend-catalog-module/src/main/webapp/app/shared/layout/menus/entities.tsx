import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/role">
      <Translate contentKey="global.menu.entities.role" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/user-role">
      <Translate contentKey="global.menu.entities.userRole" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/subject">
      <Translate contentKey="global.menu.entities.subject" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/requirement">
      <Translate contentKey="global.menu.entities.requirement" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/qualification">
      <Translate contentKey="global.menu.entities.qualification" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/teaching-staff">
      <Translate contentKey="global.menu.entities.teachingStaff" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/teaching-class">
      <Translate contentKey="global.menu.entities.teachingClass" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/paper">
      <Translate contentKey="global.menu.entities.paper" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/contact">
      <Translate contentKey="global.menu.entities.contact" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/address">
      <Translate contentKey="global.menu.entities.address" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/educational-instituition">
      <Translate contentKey="global.menu.entities.educationalInstituition" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/campus">
      <Translate contentKey="global.menu.entities.campus" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
