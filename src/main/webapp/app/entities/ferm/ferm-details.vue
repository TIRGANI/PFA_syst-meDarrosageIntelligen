<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="ferm">
        <h2 class="jh-entity-heading" data-cy="fermDetailsHeading">
          <span v-text="$t('pfaApp.ferm.detail.title')">Ferm</span> {{ ferm.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.ferm.numParcelle')">Num Parcelle</span>
          </dt>
          <dd>
            <span>{{ ferm.numParcelle }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.ferm.photo')">Photo</span>
          </dt>
          <dd>
            <span>{{ ferm.photo }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="ferm.id" :to="{ name: 'FermEdit', params: { fermId: ferm.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ferm-details.component.ts"></script>
