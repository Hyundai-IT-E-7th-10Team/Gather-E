package com.kosa.gather_e.util

import com.kosa.gather_e.model.entity.category.CategoryEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Categories {
    private  var category: List<CategoryEntity> = listOf(
        CategoryEntity("축구", 1),
        CategoryEntity("테니스", 2),
        CategoryEntity("골프", 3),
        CategoryEntity("농구", 4),
        CategoryEntity("등산", 5),
        CategoryEntity("배드민턴", 6),
        CategoryEntity("배구", 7),
        CategoryEntity("볼링", 8),
        CategoryEntity("스쿼시", 9),
        CategoryEntity("탁구", 10),
        CategoryEntity("수영", 11),
        CategoryEntity("승마", 12),
        CategoryEntity("스케이팅", 13),
        CategoryEntity("사이클링", 14),
        CategoryEntity("요가", 15),
        CategoryEntity("필라테스", 16),
        CategoryEntity("클라이밍", 17),
        CategoryEntity("당구", 18),
        CategoryEntity("댄스", 19),
        CategoryEntity("복싱", 20)
    )

    val call = SpringRetrofitProvider.getRetrofit().getCategory().enqueue(
        object: Callback<List<CategoryEntity>> {
            override fun onResponse(
                call: Call<List<CategoryEntity>>,
                response: Response<List<CategoryEntity>>
            ) {
                category = response.body()!!
            }
            override fun onFailure(call: Call<List<CategoryEntity>>, t: Throwable) {
            }
        }
    )

    fun getCategories(): List<CategoryEntity> {
        return category
    }

}