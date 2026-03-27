import React from 'react';
import { Route } from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Cliente from './cliente';
import Producto from './producto';
import CanalComercializacion from './canal-comercializacion';
import Transporte from './transporte';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="cliente/*" element={<Cliente />} />
        <Route path="producto/*" element={<Producto />} />
        <Route path="canal-comercializacion/*" element={<CanalComercializacion />} />
        <Route path="transporte/*" element={<Transporte />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
