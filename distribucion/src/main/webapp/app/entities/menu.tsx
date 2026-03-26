import React from 'react';
import { Translate } from 'react-jhipster'; // eslint-disable-line

import MenuItem from 'app/shared/layout/menus/menu-item'; // eslint-disable-line

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/cliente">
        <Translate contentKey="global.menu.entities.cliente" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/canal-comercializacion">
        <Translate contentKey="global.menu.entities.canalComercializacion" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pedido">
        <Translate contentKey="global.menu.entities.pedido" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/producto">
        <Translate contentKey="global.menu.entities.producto" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/transporte">
        <Translate contentKey="global.menu.entities.transporte" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/empaque">
        <Translate contentKey="global.menu.entities.empaque" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/separacion">
        <Translate contentKey="global.menu.entities.separacion" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
