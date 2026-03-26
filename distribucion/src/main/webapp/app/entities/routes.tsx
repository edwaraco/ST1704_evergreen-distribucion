import React from 'react';
import { Route } from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Cliente from './cliente';
import CanalComercializacion from './canal-comercializacion';
import Pedido from './pedido';
import Producto from './producto';
import Transporte from './transporte';
import Empaque from './empaque';
import Separacion from './separacion';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="cliente/*" element={<Cliente />} />
        <Route path="canal-comercializacion/*" element={<CanalComercializacion />} />
        <Route path="pedido/*" element={<Pedido />} />
        <Route path="producto/*" element={<Producto />} />
        <Route path="transporte/*" element={<Transporte />} />
        <Route path="empaque/*" element={<Empaque />} />
        <Route path="separacion/*" element={<Separacion />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
