package core.data.mapper

interface Mapper<DTO, DOMAIN> {
    fun toDomain(dto: DTO): DOMAIN
    fun toDto(domain: DOMAIN): DTO
}

fun <DTO, DOMAIN> Mapper<DTO, DOMAIN>.toDomainList(dtoList: List<DTO>): List<DOMAIN> {
    return dtoList.map { toDomain(it) }
}
