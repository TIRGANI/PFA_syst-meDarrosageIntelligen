/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import BoitierDetailComponent from '@/entities/boitier/boitier-details.vue';
import BoitierClass from '@/entities/boitier/boitier-details.component';
import BoitierService from '@/entities/boitier/boitier.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Boitier Management Detail Component', () => {
    let wrapper: Wrapper<BoitierClass>;
    let comp: BoitierClass;
    let boitierServiceStub: SinonStubbedInstance<BoitierService>;

    beforeEach(() => {
      boitierServiceStub = sinon.createStubInstance<BoitierService>(BoitierService);

      wrapper = shallowMount<BoitierClass>(BoitierDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { boitierService: () => boitierServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundBoitier = { id: 123 };
        boitierServiceStub.find.resolves(foundBoitier);

        // WHEN
        comp.retrieveBoitier(123);
        await comp.$nextTick();

        // THEN
        expect(comp.boitier).toBe(foundBoitier);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundBoitier = { id: 123 };
        boitierServiceStub.find.resolves(foundBoitier);

        // WHEN
        comp.beforeRouteEnter({ params: { boitierId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.boitier).toBe(foundBoitier);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
