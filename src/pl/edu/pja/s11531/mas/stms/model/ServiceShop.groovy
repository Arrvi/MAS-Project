package pl.edu.pja.s11531.mas.stms.model

/**
 * Repair station for spaceships
 */
class ServiceShop extends PointOfInterest {
    final Set<SpaceshipType> supportedShipTypes = new HashSet<>()

    void setSupportedShipTypes(Collection<SpaceshipType> types) {
        supportedShipTypes.clear()
        supportedShipTypes.addAll(types)
        supportedShipTypes*.link(this, false)
    }

    @Override
    protected Map<Class, String> getLinkProperties() {
        return super.getLinkProperties() + [(SpaceshipType.class): 'supportedShipTypes']
    }
}
