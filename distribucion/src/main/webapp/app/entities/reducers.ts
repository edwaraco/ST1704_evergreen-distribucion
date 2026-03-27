import cliente from 'app/entities/cliente/cliente.reducer';
import producto from 'app/entities/producto/producto.reducer';
import canalComercializacion from 'app/entities/canal-comercializacion/canal-comercializacion.reducer';
import transporte from 'app/entities/transporte/transporte.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  cliente,
  producto,
  canalComercializacion,
  transporte,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
