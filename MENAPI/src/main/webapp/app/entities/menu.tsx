import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/profil">
        <Translate contentKey="global.menu.entities.profil" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/fonction">
        <Translate contentKey="global.menu.entities.fonction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/prestation">
        <Translate contentKey="global.menu.entities.prestation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/transport">
        <Translate contentKey="global.menu.entities.transport" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/materiel">
        <Translate contentKey="global.menu.entities.materiel" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/communication">
        <Translate contentKey="global.menu.entities.communication" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/unite">
        <Translate contentKey="global.menu.entities.unite" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/donnees-references">
        <Translate contentKey="global.menu.entities.donneesReferences" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/prestation-profil">
        <Translate contentKey="global.menu.entities.prestationProfil" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/transport-profil">
        <Translate contentKey="global.menu.entities.transportProfil" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/deplacement-profil">
        <Translate contentKey="global.menu.entities.deplacementProfil" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/materiel-profil">
        <Translate contentKey="global.menu.entities.materielProfil" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
