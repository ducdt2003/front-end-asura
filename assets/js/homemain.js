
// js chuyển động đầu
document.addEventListener('DOMContentLoaded', () => {
    const carousel = document.querySelector('#comicCarousel');
    const dots = document.querySelectorAll('.scroll-dot');
  
    carousel.addEventListener('slide.bs.carousel', function (e) {
      dots.forEach(dot => dot.classList.remove('active'));
      dots[e.to].classList.add('active');
    });
  });

  
// js truyện nỗi tiếng 

document.querySelectorAll(".scroll-dot").forEach((dot, index) => {
    dot.addEventListener("click", () => {
        const scrollContainer = document.querySelector(".scroll-container");
        scrollContainer.scrollTo({
            left: index * scrollContainer.clientWidth,
            behavior: "smooth"
        });

        document.querySelectorAll(".scroll-dot").forEach(d => d.classList.remove("active"));
        dot.classList.add("active");
    });
});




