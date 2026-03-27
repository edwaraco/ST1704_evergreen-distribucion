import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CanalComercializacion from './canal-comercializacion';
import CanalComercializacionDetail from './canal-comercializacion-detail';
import CanalComercializacionUpdate from './canal-comercializacion-update';
import CanalComercializacionDeleteDialog from './canal-comercializacion-delete-dialog';

const CanalComercializacionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CanalComercializacion />} />
    <Route path="new" element={<CanalComercializacionUpdate />} />
    <Route path=":id">
      <Route index element={<CanalComercializacionDetail />} />
      <Route path="edit" element={<CanalComercializacionUpdate />} />
      <Route path="delete" element={<CanalComercializacionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CanalComercializacionRoutes;
