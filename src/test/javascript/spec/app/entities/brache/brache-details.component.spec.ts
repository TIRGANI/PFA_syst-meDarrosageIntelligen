/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import BracheDetailComponent from '@/entities/brache/brache-details.vue';
import BracheClass from '@/entities/brache/brache-details.component';
import BracheService from '@/entities/brache/brache.service';
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
  describe('Brache Management Detail Component', () => {
    let wrapper: Wrapper<BracheClass>;
    let comp: BracheClass;
    let bracheServiceStub: SinonStubbedInstance<BracheService>;

    beforeEach(() => {
      bracheServiceStub = sinon.createStubInstance<BracheService>(BracheService);

      wrapper = shallowMount<BracheClass>(BracheDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { bracheService: () => bracheServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundBrache = { id: 123 };
        bracheServiceStub.find.resolves(foundBrache);

        // WHEN
        comp.retrieveBrache(123);
        await comp.$nextTick();

        // THEN
        expect(comp.brache).toBe(foundBrache);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundBrache = { id: 123 };
        bracheServiceStub.find.resolves(foundBrache);

        // WHEN
        comp.beforeRouteEnter({ params: { bracheId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.brache).toBe(foundBrache);
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
