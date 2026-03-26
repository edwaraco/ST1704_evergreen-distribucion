import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Empaque from './empaque';
import EmpaqueDetail from './empaque-detail';
import EmpaqueUpdate from './empaque-update';
import EmpaqueDeleteDialog from './empaque-delete-dialog';

const EmpaqueRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Empaque />} />
    <Route path="new" element={<EmpaqueUpdate />} />
    <Route path=":id">
      <Route index element={<EmpaqueDetail />} />
      <Route path="edit" element={<EmpaqueUpdate />} />
      <Route path="delete" element={<EmpaqueDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmpaqueRoutes;
