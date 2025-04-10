
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
